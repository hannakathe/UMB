document.getElementById('qrForm').addEventListener('submit', function(e) {
    e.preventDefault(); // evita recargar la página al enviar el formulario

    let data = document.getElementById('data_input').value; // obtiene el texto ingresado
    let size = document.getElementById('size_input').value; // obtiene el tamaño ingresado

    let endpoint = "https://api.apgy.in/qr/?";
    let url_endpoint = endpoint + 'data=' + data + '&size=' + size; // genera la URL final

    document.getElementById('qrImage').src = url_endpoint; // asigna la imagen QR al <img>
});
