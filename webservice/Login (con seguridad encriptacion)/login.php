<?php/**
     * Guardamos nuevos usuarios
     * Devolvemos detalle del usuario
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // password encriptada
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // comprobamos que la query se ejecutó correctamente
        if ($result) {
            // obtener detalle del usuario
            $uid = mysql_insert_id(); 
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // devolvemos detalle del usuario
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Obtenemos usuario por email y contraseña
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // verificamos el resultado de la query
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            
            if ($encrypted_password == $hash) {
                // Se ha autenticado al usuario correctamente
                return $result;
            }
        } else {
            // usuario no encontrado
            return false;
        }
    }


    /**
     * Comprobamos si el usuario existe o no
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // usuario existe
            return true;
        } else {
            // usuario no existe
            return false;
        }
    }
 
    

    /**
     * Encriptamos la password
     * devolvemos el salt y la password encriptada
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Desencriptamos la password
     * Devolvemos una hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 

     // Aqui inicia la sesion
    if ($tag == 'login') {
        // chequeamos el login
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // obtenemos y comprobamos el usuario por email y password
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // usuario encontrado
            // marcamos el json como correcto con success = 1
            $response["success"] = 1;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // usuario no encontrado
            // marcamos el json con error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Email o password incorrecto!";
            echo json_encode($response);
        }
        else if ($tag == 'register') {
        // Registrar nuevo usuario
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // comprobamos si ya existe el usuario
        if ($db->isUserExisted($email)) {
            // usuario ya existe - marcamos error 2
            $response["error"] = 2;
            $response["error_msg"] = "Usuario ya existe";
            echo json_encode($response);
        } else {
            // guardamos el nuevo usuario
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // usuario guardado correctamente
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // fallo en la inserción del usuario
                $response["error"] = 1;
                $response["error_msg"] = "Ocurrió un error durante el registro";
    }
             echo json_encode($response);
            }
        }
    } else {
        echo "Petición no válida";
    }
} else {
    echo "Acceso denegado";
}
    
?>