const grid = document.getElementById('planetsGrid');
const modal = document.getElementById('planetModal');
const closeModal = document.getElementById('closeModal');
const planetName = document.getElementById('planetName');
const planetImage = document.getElementById('planetImage');
const planetDescription = document.getElementById('planetDescription');

// Cargar los planetas (limit=25)
async function loadPlanets() {
  const res = await fetch('https://dragonball-api.com/api/planets?limit=25');
  const data = await res.json();
  const planets = data.items;

  planets.forEach(planet => {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = `
      <img src="${planet.image}" alt="${planet.name}">
      <h3>${planet.name}</h3>
    `;
    card.onclick = () => showPlanetDetail(planet.id);
    grid.appendChild(card);
  });
}

// Mostrar detalle del planeta
async function showPlanetDetail(id) {
  const res = await fetch(`https://dragonball-api.com/api/planets/${id}`);
  const planet = await res.json();

  planetName.textContent = planet.name;
  planetImage.src = planet.image;
  planetDescription.textContent = planet.description || 'Sin descripción disponible.';

  modal.style.display = 'flex';
}

closeModal.onclick = () => {
  modal.style.display = 'none';
};

window.onclick = (e) => {
  if (e.target === modal) {
    modal.style.display = 'none';
  }
};

loadPlanets();
