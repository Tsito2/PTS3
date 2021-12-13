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

	    
	    if(isset($_POST['classe'])){
	    	// Définition de la requête
	    	$sql = "SELECT * FROM ELEVE WHERE idClasse IN ( SELECT idClasse FROM CLASSE WHERE nomClasse = '". $_POST['classe']."')";
			//$sql = "select * from  where nomProfesseur = '" . $_POST['classe'] . "' and password = '" . $_POST['password'] . "'";
	    }else if(isset($_GET['classe'])){
	    	$sql = "SELECT * FROM ELEVE WHERE idClasse IN ( SELECT idClasse FROM `CLASSE` WHERE nomClasse = '5ème D' )";
	    	echo $sql."<br><br><br>";
	    }else {
	    	printf($json);
	    	die();
	    }

		//$sql = "select * from PROFESSEUR where nomProfesseur = 'ernet' and password = 'be'"; // pour le test de la page seulement
		
		// Envoi de la requête
		$response = $bdd->query($sql);
		
		// Traitement de la réponse
		if ($response != null) { // le serveur a répondu
			
			// Obtention dans un tableau de la réponse (une ligne qui contient toutes les colonnes)
			$array = $response->fetchAll(PDO::FETCH_ASSOC);
			
			// Le nombre de lignes retourné est-il égal à un
			if (count($array) > 0) { // oui, donc le prof est authentifié
					$json = json_encode($array);
				// Pas nécessaire, exemple qui récupère les noms des colonnes
				// Obtention de l'id du prof pour la requête suivante
				//$json = json_encode(array("nomProfesseur" => $username, "password" => $password));
				//$json = json_encode(array('nomProfesseur' => $profName));
				
				// Encodage JSON				
				
			}else if (count($array) == 0) {
				$json = "CLASS NOT FOUND";
			}
    	}
    }
	catch (Exception $e) {
    	die('Error : ' . $e->getMessage());
	}
	
	// Envoi de la réponse
	printf($json);
?>
