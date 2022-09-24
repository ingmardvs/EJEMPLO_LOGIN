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
    
        $idalumnoo['Almnos']=array();
            while($filas=$query->fetch_object())  
            {
               $resultados=array();
               $resultados['idalumno']=$filas->idalumno;
               $resultados['nombre']=$filas->nombre;
               $resultados['carrera']=$filas->carrera;
               $resultados["ruta_imagen"]=$filas->ruta_imagen;
                array_push($idalumnoo['Almnos'],$resultados);
                
               
            }
            
            print json_encode($idalumnoo);
            
            
       
