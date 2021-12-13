let idCaracteristica

function includeHTML() {
    var z, i, elmnt, file, xhttp;
    /* Loop through a collection of all HTML elements: */
    z = document.getElementsByTagName("*");
    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        /*search for elements with a certain atrribute:*/
        file = elmnt.getAttribute("w3-include-html");
        if (file) {
            /* Make an HTTP request using the attribute value as the file name: */
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4) {
                    if (this.status == 200) {elmnt.innerHTML = this.responseText;}
                    if (this.status == 404) {elmnt.innerHTML = "Page not found.";}
                    /* Remove the attribute, and call this function once more: */
                    elmnt.removeAttribute("w3-include-html");
                    includeHTML();
                }
            }
            xhttp.open("GET", file, true);
            xhttp.send();
            /* Exit the function: */
            return;
        }
    }
}
function confirmarEliminacion(id){
    document.getElementById("caracteristicaMascotaId").value = id;
    document.getElementById("modalEliminar").style.display = 'block';
}

function eliminarCaracteristica(id){
    if(!confirm("Esta seguro que desea eliminar esta caracteristica?"))
        return;
    $.ajax({
        type: "DELETE",
        url: "caracteristicas/" + id,
        success: function(result){
            location.reload(true);
        }
    });
}

function mostrarModal(id){
    $("#caracteristicaModal").modal("show")
    console.log($("#id"))
    $("#id").val(id)
}

function guardarModal(){
    $.ajax({
        type: "POST",
        url: "caracteristicas",
        data:{id:idCaracteristica, descripcion:$("#caracteristica").val()},
        success: function(result){
            location.reload(true);
        }
    });
}

function cerrarModal(){
    document.getElementsByClassName("modalEliminar modal")[0].style.display = 'none';
}