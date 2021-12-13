let idCaracteristica

function openPublis(evt, cityName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}

function confirmarEliminacion(id){
    document.getElementById("caracteristicaMascotaId").value = id;
    document.getElementById("modalEliminar").style.display = 'block';
}

function eliminarPublicacion(id){
    if(!confirm("Esta seguro que desea eliminar esta publicacion?"))
        return;
    $.ajax({
        type: "DELETE",
        url: "misPublicaciones/" + id,
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
        url: "misPublicaciones",
        data:{id:idCaracteristica, descripcion:$("#caracteristica").val()},
        success: function(result){
            location.reload(true);
        }
    });
}

function cerrarModal(){
    document.getElementsByClassName("modalEliminar modal")[0].style.display = 'none';
}


function redirigirDetallePublicacion(id) {
    window.location.href = '/detallePublicacion/'+id;
}

