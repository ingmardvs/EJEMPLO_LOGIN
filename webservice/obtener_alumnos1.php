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
            $idalumnoo["estado"]=1;		// cambio "1" a 1 porque no coge bien la cadena.
            // Enviar objeto json del alumno
            
            $idalumnoo["alumnos"]=array();
        
            while($filas=$query->fetch_object())  
            {
               $resultados=array();
               $resultados["idalumno"]=$filas->idalumno;
               $resultados["nombre"]=$filas->nombre;
               $resultados["carrera"]=$filas->carrera;
                array_push($idalumnoo["alumnos"],$resultados);
                
               
            }
            
            print json_encode($idalumnoo);
            
            
        }
        else{
            
             $idalumnoo["estado"]=2;		// cambio "1" a 1 porque no coge bien la cadena.
            // Enviar objeto json del alumno
            print json_encode($idalumnoo);
            }
        
    

?>