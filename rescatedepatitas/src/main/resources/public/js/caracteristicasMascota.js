let listaCaracteristicas = [];
function fnAgregarCaracteristica() {
    if(!validarCaracteristica())
        return;

    $("#caracteristicas").unbind("change");

    var i = 0;

    if (listaCaracteristicas)
        for (var j = 0; j < listaCaracteristicas.length; j++)
            i = listaCaracteristicas[j].index;

    i++;

    let catacteristica = getCaracteristica(i);
    listaCaracteristicas.push(catacteristica);

    $("#selectCaracteristicas option:selected").removeAttr("selected");

    $("#selectCaracteristicas").append($('<option>', {
        value: catacteristica.index,
        text: `${catacteristica.descCaracteristica} ${catacteristica.valor}`,
        selected: "selected",
        index: i
    }));

    $("#caracteristica").val("");
    $("#caracteristica-valor").val("");
}

function validarCaracteristica(){
    if($("#caracteristica").val() == ''){
        $("#caracteristica").focus();
        return false;
    }

    if($("#caracteristica-valor").val() == ''){
        $("#caracteristica-valor").focus();
        return false;
    }

    return true;
}

function getCaracteristica(i) {
    let caracteristica = {
        index: i,
        idCaracteristica: $("#caracteristica").val(),
        descCaracteristica: $("#caracteristica option:selected").text(),
        valor: $("#caracteristica-valor").val(),
        
    };

    return caracteristica;
}

function onSubmit() {
    $.each(listaCaracteristicas, function (i, v) {
        var input = $("<input>").attr({ "type": "hidden", "name": "caracteristicas[]" }).val(serialize(v));
        $('form').append(input);
    });
}

function fnRemoverCaracteristica() {
    if ($("#selectCaracteristicas option:selected").length > 0 && confirm("¿Desea borrar la caracteristica seleccionada?")) {
        var option = $("#selectCaracteristicas option:selected")[0];

        var index_del = $(option)[0].index;
        listaCaracteristicas.splice(index_del, 1);
        $("#selectCaracteristicas option:selected").remove();
    }
}

serialize = function (obj) {
    var str = [];
    for (var p in obj)
        if (obj.hasOwnProperty(p)) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
    return str.join("&");
}