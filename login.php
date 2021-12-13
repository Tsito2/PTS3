<?php
	$json = "NOK";
	
	try {
		// Défintion de l'URL de la base de données et des identifiants
		try{
   			$bdd = new PDO("mysql:host=localhost;dbname=inf2pj_01", "inf2pj01", "choj7oa9iu");
		} catch (PDOException $e){
		    printf($json);
		    die();
		}

	    
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
				
				//$json = json_encode(array("nomProfesseur" => $username, "password" => $password));
				//$json = json_encode(array('nomProfesseur' => $profName));
				
				// Obtention de la liste des classes (id de la classe + nom de la classe) du prof
				$sql = "select idClasse, nomClasse from PROFESSEUR inner join CLASSE on PROFESSEUR.idProfesseur = CLASSE.idProfesseur where PROFESSEUR.idProfesseur = '$profId'";
				
				// Envoi de la requête
				$response = $bdd->query($sql);
				
				// Obtention dans un tableau de la réponse (la réponse peut contenir plusieurs lignes
				$array = $response->fetchAll(PDO::FETCH_ASSOC);
				
				// Encodage JSON				
				$json = json_encode(array('nomProfesseur' => $profName, 'classes' => array_values($array)));
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
?>
