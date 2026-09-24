document.addEventListener('DOMContentLoaded', function () {
    const menuToggle = document.getElementById('menuToggle');
    const navMenu = document.getElementById('navMenu');
  
    if (menuToggle && navMenu) {
      menuToggle.addEventListener('click', function () {
        navMenu.classList.toggle('active');
      });
  
      // Fecha o menu ao clicar em qualquer link
      document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
          navMenu.classList.remove('active');
        });
      });
    }
  });