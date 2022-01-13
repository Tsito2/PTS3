<?php
	$json = "NOK";
	
	try {
		// Défintion de l'URL de la base de données et des identifiants
		try{
   			$bdd = new PDO("mysql:host=localhost;dbname=inf2pj_01", "inf2pj01", "choj7oa9iu");
			$GLOBALS['bdd'] = $bdd;
		} catch (PDOException $e){
		    printf($json);
		    die();
		}
		echo $bdd == null;

	    
	    if (!isset($_POST['login']) || !isset($_POST['password'])){
	    	if (isset($_GET['login']) && isset($_GET['password'])){
	    		$sql = "select * from PROFESSEUR where nomProfesseur = '" . $_GET['login'] . "' and password = '" . $_GET['password'] . "'";
	    	}
	    }else{
	    	// Définition de la requête
			$sql = "select * from PROFESSEUR where nomProfesseur = '" . $_POST['login'] . "' and password = '" . $_POST['password'] . "'";
	    }

		//$sql = "select * from PROFESSEUR where nomProfesseur = 'ernet' and password = 'be'"; // pour le test de la page seulement
		
		// Envoi de la requête
		$response = $bdd->query($sql);
		
		// Traitement de la réponse
		if ($response != null) { // le serveur a répondu
			
			// Obtention dans un tableau de la réponse (une ligne qui contient toutes les colonnes)
			$array = $response->fetchAll(PDO::FETCH_ASSOC);
			
			// Le nombre de lignes retourné est-il égal à un
			if (count($array) == 1) { // oui, donc le prof est authentifié
				
				// Pas nécessaire, exemple qui récupère les noms des colonnes
				$nb_columns = $response->columnCount();
				
				for ($i = 0; $i < $nb_columns; $i++) {
					$meta = $response->getColumnMeta($i);
					$column[$i] = $meta['name'];
				}

				// Obtention de l'id du prof pour la requête suivante
				$profId  = $array[0]['idProfesseur'];
				
				// Obtention du nom du prof
				$profName = $array[0]['nomProfesseur'];
				$profForName = $array[0]['prenomProfesseur'];
				
				
				//$json = json_encode(array("nomProfesseur" => $username, "password" => $password));
				//$json = json_encode(array('nomProfesseur' => $profName));
				
				// Obtention de la liste des classes (id de la classe + nom de la classe) du prof
				$classesListe = getClassesFromTeacher($profId);
				$sportsListe = getAllSports();
				$classes = array();
				$eleves = array();

				foreach ($classesListe as $classe){
					$elevesListe = getStudentsFromClass($classe['idClasse']);
					foreach ($elevesListe as $eleve){
						$eleve['notes'] = getNotesFromEleve($eleve['idEleve']);
						array_push($eleves, $eleve);
					}
					$classe['eleves'] = $eleves;
					array_push($classes, $classe);
				}

				$sports = array();
				$competences = array();
				$gcriteres = array();
				$champ_apprentissage = array();

				foreach ($sportsListe as $sport){
					$competencesListe = getCompetencesFromSport($sport['idSport']);
					
					foreach ($competencesListe as $competence){
						$gcriteresListe = getGCriteresFromCompetence($competence['idCompetence']);

						foreach ($gcriteresListe as $gcritere){
							$gcritere['criteres'] = getCritereFromGCriteres($gcritere['idGCriteres']);
							array_push($gcriteres, $gcritere);
						}
						$competence['attendus'] = getAttendusFromCompetence($competence['idCompetence']);
						$competence['gcompetences'] = getGCFromCompetence($competence['idCompetence']);
						$competence['gcriteres'] = $gcriteres;
						array_push($competences, $competence);
					}
					$sport['competences'] = $competences;
					$sport['champ_apprentissage'] = getCAFromSport($sport['idSport']);
					array_push($sports, $sport);
				}
				
				
				// Encodage JSON				
				$json = json_encode(array('nomProfesseur' => $profName, 'prenomProfesseur' => $profForName, 'classes' => array_values($classes), 'sports' => array_values($sports)));
				
			}else if (count($array) == 0) {
				$json = "LOGIN NOT FOUND";
			}
    	}
    }
	catch (Exception $e) {
    	die('Error : ' . $e->getMessage());
	}
	
	// Envoi de la réponse
	printf($json);

	function getAllSports(){
		$sql = "SELECT * FROM SPORT";
		$response = $GLOBALS['bdd']->query($sql);
		
		
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getSportsFromCLass($classId){
		$sql = "SELECT SPORT.idSport, ENSEIGNE.idEnseigne, SPORT.idCA, SPORT.nomSport, SPORT.descriptionSport, nb_lecons, duree FROM SPORT JOIN ENSEIGNE ON ENSEIGNE.idSport = SPORT.idSport JOIN CLASSE ON ENSEIGNE.idClasse = CLASSE.idClasse WHERE CLASSE.idClasse = ".$classId;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	

	function getNotesFromEleve($idEleve){
		$sql = "SELECT idNote, idCritere, note FROM NOTER WHERE idEleve = ".$idEleve;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}


	function getCritereFromGCriteres($idGCritere){
		$sql = "SELECT idCritere, type, descriptionCritere, points FROM CRITERES WHERE idGCriteres = ".$idGCritere;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getAttendusFromCompetence($idCompetence){
		$sql = "SELECT idAttendus, descriptionAttendus FROM ATTENDUS WHERE idCompetence = ".$idCompetence;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getGCriteresFromCompetence($idCompetence){
		$sql = "SELECT idGCriteres, descriptionGCriteres, statut_evaluation FROM GROUPE_CRITERES WHERE idCompetence = ".$idCompetence;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getGCFromCompetence($idCompetence){
		$sql = "SELECT COMPETENCE.idGC, descriptionGC FROM GROUPE_COMPETENCE JOIN COMPETENCE ON COMPETENCE.idGC = GROUPE_COMPETENCE.idGC WHERE COMPETENCE.idCompetence = ".$idCompetence;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getCAFromSport($sportId){
		$sql = "SELECT SPORT.idCA, nomCA FROM CHAMP_APPRENTISSAGE JOIN SPORT ON SPORT.idCA = CHAMP_APPRENTISSAGE.idCA WHERE SPORT.idSport = ".$sportId;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}

	function getCompetencesFromSport($sportId){
		$sql = "SELECT EVALUE.idCompetence, descriptionCompetence, idGC FROM COMPETENCE JOIN EVALUE ON EVALUE.idCompetence = COMPETENCE.idCompetence WHERE EVALUE.idSport = ".$sportId;
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	
	}

	function getStudentsFromClass($classId){
		$sql = "SELECT * FROM ELEVE WHERE idClasse IN ( SELECT idClasse FROM CLASSE WHERE idClasse = '". $classId."')";
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	 }


	function getClassesFromTeacher($profId){
		
		$sql = "select idClasse, nomClasse from PROFESSEUR inner join CLASSE on PROFESSEUR.idProfesseur = CLASSE.idProfesseur where PROFESSEUR.idProfesseur = '$profId'";
				
		// Envoi de la requête
		$response = $GLOBALS['bdd']->query($sql);
		$array = $response->fetchAll(PDO::FETCH_ASSOC);
		return $array;
	}
?>
