$(document).ready(function () {
    $("#success-alert").hide();
    $("#myWish").click(function showAlert() {
        $("#success-alert").fadeTo(2000, 500).slideUp(500, function () {
            $("#success-alert").slideUp(500);
        });
    });
});


function cargarFiltrosYOrdenar() {
    var caracteristicas = $(".caracteristica-filtro").filter((i, item) => item.value != '').map(function (i, item) {
        return $(item).attr("caracteristica") + ":" + item.value;
    });

    var obj = {
        sexo: $('input[name="sexoFiltro"]:checked').val(),
        ordenar: $("#sort").val(),
        especie: $("#especie").val(),
        raza: $("#raza").val(),
        caracteristicas: caracteristicas.toArray()
    };

    window.location.href = `?${$.param(obj)}`
}



function abreSitio() {
    var URL = "http://localhost:9000/adoptarMascota";
    var web = document.form1.sitio.options[document.form1.sitio.selectedIndex].value;
    // open(URL+web);
    window.location.href = URL + web
}

function borrarFiltroSexo() {
    $("[name='sexoFiltro']:checked").checked = false;
}

function esMiMascota(id) {
    $.ajax({
        type: "POST",
        url: "esMiMascota?id=" + id,
        success: function (result) {
            if (result.exito === true) {
                $(window).scrollTop(0);
                $("#success-alert").fadeTo(6000, 500).slideUp(2000, function () {
                    $("#success-alert").slideUp(500);
                });
            }
            else if(result.exito == false)
                alert("Ocurrio un error al intentar notificar al rescatista. Intente de nuevo en unos minutos.") 
            else
                window.location.href = "/loginDuenio"
        }
    });
}

function quieroAdoptar(id) {
    // var new_form = jQuery('<form>', {
    //     'action': "quieroAdoptar?id=" + id,
    //     'method': 'POST',
    // });
    // $(document.body).append(new_form);
    // new_form.submit();
    $.ajax({
        type: "POST",
        url: "quieroAdoptar?id=" + id,
        success: function (result) {
            if (result.exito === true) {
                $(window).scrollTop(0);
                $("#success-alert").fadeTo(6000, 500).slideUp(2000, function () {
                    $("#success-alert").slideUp(500);
                });
            }
            else if(result.exito == false)
                alert("Ocurrio un error al intentar notificar al rescatista. Intente de nuevo en unos minutos.") 
            else
                window.location.href = "/loginDuenio"
        }
    });
}
