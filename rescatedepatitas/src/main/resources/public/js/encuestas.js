let idCaracteristica

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
        url: "/encuesta/encuestaActiva/respuesta",
        data:{id:idCaracteristica, descripcion:$("#caracteristica").val()},
        success: function(result){
            location.reload(true);
        }
    });
}

function cerrarModal(){
    document.getElementsByClassName("modalEliminar modal")[0].style.display = 'none';
}