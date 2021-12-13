function ValidarFormulario(){
   // alert("todo ok");

   var formulario = document.loginForm;
   var user = formulario.usuario.value;
   var pass = formulario.password.value;
   if(user == "" && pass == ""){
       alert("Por favor, ingrese sus datos");
       return false;
   }
   if(user == ""){
       alert("Por favor, ingrese su nombre de usuario");
       return false;
   }
   if(pass == ""){
    alert("Por favor, ingrese su contraseña");
    return false;
} 

  formulario.submit();

}