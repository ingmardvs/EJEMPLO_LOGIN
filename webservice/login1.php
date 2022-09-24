<?php
$entityBody=file_get_contents('php://input');
$dbHost="localhost";
$dbUsername="root";
$dbPassword="";
$dbName="alumn";
$idalumnoo=array();
$db = new mysqli($dbHost,$dbUsername,$dbPassword,$dbName);

if ($db->connect_error){
    die("Conexión fallida: ". $db->connect_error);
}

    if(isset($_GET['idalumno']))
    {
        
        $idalumno=$_GET['idalumno'];
        $clave=$_GET['clave'];
        $consulta=("SELECT * FROM users where usuario = '$idalumno' and clave = '$clave'");
        $query=$db->query($consulta);
        $result=$query->num_rows;

        
        if($result>0)
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
        
    }


?>