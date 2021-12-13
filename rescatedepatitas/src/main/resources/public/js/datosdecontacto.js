let listaDatosDeContacto = [];

$(function () {
  $('#datos option').dblclick(function () {
    var option = $("#datos option:selected")[0];
    abrirModelEditar($(option)[0].index);
  });
});

function fnAgregarDato() {
  if (!validarDato())
    return;


  $("#datos").unbind("change");

  var i = 0;

  if (listaDatosDeContacto)
    for (var j = 0; j < listaDatosDeContacto.length; j++)
      i = listaDatosDeContacto[j].index;

  i++;

  let datoDeContacto = getDatoContacto(i);
  listaDatosDeContacto.push(datoDeContacto);
  console.log(listaDatosDeContacto);

  $("#datos option:selected").removeAttr("selected");

  $("#datos").append($('<option>', {
    value: datoDeContacto.index,
    text: datoDeContacto.contName,
    selected: "selected",
    index: i
  }).dblclick(function () {
    var option = $("#datos option:selected")[0];
    abrirModelEditar($(option)[0].index);
  }));

  $("#datoDeContactoModal").modal("hide");
  $(".divDatos input").val("")
  $(".divDatos input[type='checkbox']").prop('checked', false);

}

function abrirModelCrear() {
  $("#aceptarModal").attr("onclick", "fnAgregarDato()");
  $("#datoDeContactoTitle").html("Nuevo dato de contacto");
  $("#datoDeContactoModal").modal("show");
}

function abrirModelEditar(index) {
  datos_seleccionados = listaDatosDeContacto[index];
  $("#contName").val(datos_seleccionados.contName)
  $("#contTel").val(datos_seleccionados.contTel)
  $("#contMail").val(datos_seleccionados.contMail)
  $("#whatsapp").prop("checked", datos_seleccionados.whatsapp)
  $("#mail").prop("checked", datos_seleccionados.mail)
  $("#sms").prop("checked", datos_seleccionados.sms)
  $("#aceptarModal").attr("onclick", "fnEditarDatos()");
  $("#datoDeContactoTitle").html("Editar dato de contacto");
  $("#datoDeContactoModal").modal("show");
  $("#indexDatos").val(index);
}

function validarDato() {
  if ($("#contName").val() == '') {
    alert("Ingrese nombre de contacto");
    $("#contName").focus();
    return false;
  }
  //agregar más validaciones

  if (($("#contTel").val() + $("#contMail").val()) == '') {
    alert("Ingrese telefono o mail");
    $("#contTel").focus();
    return false;
  }

  return true;
}

function getDatoContacto(i) {
  let datoContacto = {
    index: i,
    contName: $("#contName").val(),
    contTel: $("#contTel").val(),
    contMail: $("#contMail").val(),
    whatsapp: $("#whatsapp").is(":checked"),
    mail: $("#mail").is(":checked"),
    sms: $("#sms").is(":checked")
  };

  //DALE LA CONCHA DE TU MADREEEE
  if (listaDatosDeContacto[i] && listaDatosDeContacto[i].hasOwnProperty("id")) {
    datoContacto.id = listaDatosDeContacto[i].id;
  }

  return datoContacto;
}

function validarForm() {
  if (!listaDatosDeContacto.length) {
    alert("Ingrese por lo menos 1 dato de contacto");
    $("#contName").focus()
    return false;
  }
  //agregar más validaciones

  return true;
}

function onSubmit(e) {
  console.log(e);
  if (!validarForm()) {
    e.preventDefault();
    return false;
  }
  $.each(listaDatosDeContacto, function (i, v) {
    var input = $("<input>").attr({ "type": "hidden", "name": "datitos[]" }).val(serialize(v));
    $('form').append(input);
  });
}

function fnRemoverDatos() {
  if ($("#datos option:selected").length > 0 && confirm("¿Desea borrar los datos de contacto seleccionados?")) {
    var option = $("#datos option:selected")[0];

    var index_del = $(option)[0].index;
    listaDatosDeContacto.splice(index_del, 1);
    $("#datos option:selected").remove();
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

function convertirAModelo(obj, index) {
  let datoContacto = {
    index: index,
    contName: obj.nombreYApellido,
    contTel: obj.telefono,
    contMail: obj.email,
    whatsapp: obj.formasDeNotificacion.some(f => f.notificadorPorWhatsApp),
    mail: obj.formasDeNotificacion.some(f => f.notificadorPorEmail),
    sms: obj.formasDeNotificacion.some(f => f.notificadorPorSMS),
    id: obj.id
  };

  return datoContacto;
}

function fnEditarDatos() {
  if (!validarDato())
    return;


  $("#datos").unbind("change");

  let index = parseInt($("#indexDatos").val());
  let datoDeContacto = getDatoContacto(index);
  listaDatosDeContacto[index] = datoDeContacto;
  console.log(listaDatosDeContacto);

  $("#datos option:selected").removeAttr("selected");

  $($("#datos option")[index]).text(datoDeContacto.contName);
  $($("#datos option")[index]).attr("selected", "selected");

  $("#datoDeContactoModal").modal("hide");
  $(".divDatos input").val("")
  $(".divDatos input[type='checkbox']").prop('checked', false);
}

function registrarUsuario() {
  if (!$("form")[0].reportValidity() || !validarForm()) {
    return
  }
  var form = $('form');
  $.each(listaDatosDeContacto, function (i, v) {
    var input = $("<input>").attr({ "type": "hidden", "name": "datitos[]" }).val(serialize(v));
    form.append(input);
  });

  $.ajax({
    type: "POST",
    url: "/register",
    data: form.serialize(),
    success: function (result) {
      if (result.exito === true) {
        window.location.href = "/loginDuenio"
      }
      else if (result.exito == false)
        alert(result.mensajeError);
    }
  });
}