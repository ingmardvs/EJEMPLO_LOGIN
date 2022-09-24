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

        $consulta=("SELECT * FROM alumnos");
        $query=$db->query($consulta);
        $result=$query->num_rows;
        if($result>0)
        {
            $idalumnoo["alumnos"]=array();
            while($filas=$query->fetch_object())  
            {
               $resultados=array();
               $resultados["idalumno"]=$filas->idalumno;
                array_push($idalumnoo["alumnos"],$resultados);
                
               
            }
            
            print json_encode($idalumnoo);
            
            
        }
       
        
    

?>