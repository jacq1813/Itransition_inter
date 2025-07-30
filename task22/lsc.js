const { sha3_256 } = require('js-sha3');
const fs = require('fs/promises');
const path = require('path');

const correo = "jacqrr13@gmail.com";
const rutas = './task2';

async function procesar() {
  try {
    const archivos = await fs.readdir(rutas);

    const hashes = await Promise.all(
      archivos.map(async archivo => {
        const file = path.join(rutas, archivo);
        const datos = await fs.readFile(file);
        return sha3_256(datos);
      })
    );

    hashes.sort((a, b) => b.localeCompare(a));

    const joinedHash = hashes.join('');
    const final = sha3_256(joinedHash + correo);

    console.log(final);
  } catch (err) {
    console.error('Error:', err);
  }
}

procesar();
