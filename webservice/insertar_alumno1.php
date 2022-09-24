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
        $nombre=$json['nombre'];
        $carrera=$json['carrera'];

        $consulta=("INSERT INTO alumnos (idalumno, nombre, carrera) VALUES ('{$idalumno}','{$nombre}','{$carrera}')");
        $query=$db->query($consulta);

        
        if($query)
        {
            $idalumnoo["estado"]=1;		// cambio "1" a 1 porque no coge bien la cadena.
            // Enviar objeto json del alumno
            print json_encode($idalumnoo);
            
        }
        else{
            
             $idalumnoo["estado"]=2;		// cambio "1" a 1 porque no coge bien la cadena.
            // Enviar objeto json del alumno
            print json_encode($idalumnoo);
        }
           
       
?>