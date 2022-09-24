<?PHP
$hostname_localhost="localhost";
$database_localhost="alumn";
$username_localhost="root";
$password_localhost="";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	$nombre = $_POST["nombre"];
	$idalumno = $_POST["codigo"];
	$carrera = $_POST["carrera"];
	$imagen = $_POST["imagen"];

	$path = "imagenes/$nombre.jpg";

	//$url = "http://200.170.1.39/WebService/$path";
	$url = "imagenes/".$nombre.".jpg";

	file_put_contents($path,base64_decode($imagen));
	$bytesArchivo=file_get_contents($path);

   // $sql="UPDATE usuario SET nombre= ? , profesion= ?, imagen=?, ruta_imagen=? WHERE documento=?";
	//$stm=$conexion->prepare($sql);
	//$stm->bind_param('ssssi',$nombre,$profesion,$bytesArchivo,$url,$documento);

//$query = "UPDATE customers SET CUST_ID='$id',$CUST_FORENAME='$name',
         //   CUST_PHONE='$phone', CUST_SURNAME='$surname' WHERE CUST_ID=$id";


	$sql="INSERT INTO alumnos VALUES (?,?,?,?,?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('issss',$idalumno,$nombre,$carrera,$bytesArchivo,$url);
		
	if($stm->execute()){
		echo "registra";
	}else{
		echo "noRegistra";
	}
	
  mysqli_close($conexion);
?>

