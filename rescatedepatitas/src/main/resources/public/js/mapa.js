function initAutocomplete() {
  var suffix = '';
  var map = new google.maps.Map(document.getElementById('map' + suffix), {
    center: { lat: -34.6144934119689, lng: -58.4458563545429 },
    zoom: 10,
    mapTypeId: 'roadmap'
  });

  // Create the search box and link it to the UI element.
  var input = document.getElementById('pac-input' + suffix);
  var searchContainer = document.getElementById('search-container' + suffix);
  // map.controls[google.maps.ControlPosition.TOP_LEFT].push(searchContainer);

  var searchBox = new google.maps.places.SearchBox(input);
  //map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  // Bias the SearchBox results towards current map's viewport.
  map.addListener('bounds_changed', function () {
    searchBox.setBounds(map.getBounds());
  });

  $("#pac-input").keydown(function (e) {

    if (e.which == 13) {
      google.maps.event.trigger(searchBox, 'place_changed');
      return false;
    }
  });

  var markers = [];
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener('places_changed', function () {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }

    // Clear out the old markers.
    markers.forEach(function (marker) {
      marker.setMap(null);
    });
    markers = [];

    if (places.length == 1) {
      $("#pac-input").val(places[0].formatted_address);
      let longitud = places[0].geometry.location.lng();
      let latitud = places[0].geometry.location.lat();
      $("#lblLongitud").html(longitud);
      $("#longitud").val(longitud);
      $("#lblLatitud").html(latitud);
      $("#latitud").val(latitud);
    }

    // For each place, get the icon, name and location.
    var bounds = new google.maps.LatLngBounds();
    places.forEach(function (place) {
      if (!place.geometry) {
        //console.log("Returned place contains no geometry");
        return;
      }
      var icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      markers.push(new google.maps.Marker({
        map: map,
        icon: icon,
        title: place.name,
        position: place.geometry.location
      }));

      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
      markers.forEach(function (marker) {
        google.maps.event.addListener(marker, 'click', function (event) { onClick(event) });
      });
    });
    map.fitBounds(bounds);
  });

  google.maps.event.addListener(map, "tilesloaded", function () {
    [].slice.apply(document.querySelectorAll('#map' + suffix + ' *')).forEach(function (item) {
      item.setAttribute('tabindex', '-1');
    });
  })

  var geocoder = new google.maps.Geocoder();

  google.maps.event.addListener(map, 'click', function (event) { onClick(event) });

  function onClick(event) {
    markers.forEach(function (marker) {
      marker.setMap(null);
    });
    markers = [];
    geocoder.geocode({
      'latLng': event.latLng
    }, function (results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        if (results[0]) {

          var icon = {
            url: results[0].icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
          };

          // Create a marker for each place.
          markers.push(new google.maps.Marker({
            map: map,
            icon: icon,
            title: results[0].name,
            position: results[0].geometry.location
          }));

          $("#pac-input").val(results[0].formatted_address);
          let longitud = results[0].geometry.location.lng();
          let latitud = results[0].geometry.location.lat();
          $("#lblLongitud").html(longitud);
          $("#longitud").val(longitud);
          $("#lblLatitud").html(latitud);
          $("#latitud").val(latitud);

        }
      }
    });
  }
}

function changeMapTabIndex() {
  [].slice.apply(document.querySelectorAll('#map *')).forEach(function (item) {
    item.setAttribute('tabindex', '-1');
  });
}