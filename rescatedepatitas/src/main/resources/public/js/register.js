var cantContactos = 0;

function ValidarFormularioRegister(){
 
    var formulario = document.registerForm;
    var user = formulario.user.value;
    var pass = formulario.pass.value;
    var repass = formulario.repass.value;
    var nombre = formulario.nYA.value;
    var nac = formulario.nac.value;
    var dni = formulario.dni.value;


    if(pass != repass){
        alert("Las Contraseñas no coinciden");
        return false;
    }


    /*
    if(cantContactos == 0){
        alert("Debe añadir al menos un dato de contacto");
    }
 */
   //formulario.submit();
 
 }

 function aniadirContacto(){
    
    var formulario = document.registerForm;
    var nomCont   = formulario.contName.value;
    var telCont   = formulario.contTel.value;
    var mailCont  = formulario.contMail.value;
    var notifCont = formulario.contNotif.value;
   

    if(nomCont == "" ||telCont == "" ||mailCont == ""){
        alert("Por favor, ingrese los datos en todos los campos de contacto");
        
    }else{
    
    if(confirm("los datos de contacto son correctos? \n Nombre: " + nomCont + "\n Telefono: " + telCont + "\n E-Mail: " + mailCont + "\n Forma de contacto Favorita: " + notifCont)){
        cantContactos ++;
        alert("Contacto añadido, cantida de contactos: \n" + cantContactos);
    }
}

 }