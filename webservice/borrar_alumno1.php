<?php
$entityBody=file_get_contents('php://input');
$json=json_decode($entityBody, true);
$dbHost="localhost";
$dbUsername="root";
$dbPassword="";
$dbName="alumn";
$idalumnoo=array();
$db = new mysqli($dbHost,$dbUsername,$dbPassword,$dbName);

if ($db->connect_error){
    die("Conexión fallida: ". $db->connect_error);
}
        
        $idalumno=$json['idalumno'];  
        $consulta=("SELECT * FROM alumnos where idalumno = '$idalumno' ");
        $query=$db->query($consulta);
        $result=$query->num_rows;
        if ($result > 0) 
            {
                $consulta=("DELETE FROM alumnos where idalumno = '$idalumno'");
                $query=$db->query($consulta);
                $idalumnoo["estado"]=1;		// cambio "1" a 1 porque no coge bien la cadena.
                // Enviar objeto json del alumno
                print json_encode($idalumnoo);
            
            } 
        else
            {
            
                $alumnoo["estado"] = 2;
                echo json_encode($alumnoo);
            
            }
    
 
           
       
?>