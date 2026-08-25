// Function para alterar entre o cadastro e o login
function switchTab(tab) {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const loginTab = document.getElementById('login-tab');
    const registerTab = document.getElementById('register-tab');

    if (tab === 'login') {
        loginForm.classList.add('active');
        registerForm.classList.remove('active');
        loginTab.classList.add('active');
        registerTab.classList.remove('active');
    } else if (tab === 'register') {
        registerForm.classList.add('active');
        loginForm.classList.remove('active');
        registerTab.classList.add('active');
        loginTab.classList.remove('active');
    }
}

// Garantir que o DOM está carregado antes de manipular os elementos
document.addEventListener("DOMContentLoaded", function () {
    const formRegister = document.getElementById("register-form");

    if (formRegister) {
        formRegister.addEventListener("submit", function (event) {
            // Bloqueia o envio do formulário IMEDIATAMENTE
            event.preventDefault();

            const nome = document.getElementById("register-name").value.trim();
            const email = document.getElementById("register-email").value.trim();
            const senha = document.getElementById("register-password").value;
            const confirmSenha = document.getElementById("confirm-password").value;

            // Debug para você conferir os valores exatos no Console (F12)
            console.log("Senha:", `"${senha}"`);
            console.log("Confirmar Senha:", `"${confirmSenha}"`);

            // Validação estrita
            if (senha !== confirmSenha) {
                alert("As senhas são diferentes! Corrija para continuar.");
                return; // PARALISA A EXECUÇÃO AQUI
            }

            // Código só chega aqui se as senhas forem rigorosamente iguais
            const usuario = {
                nome: nome,
                email: email,
                senha: senha
            };

            console.log("Usuário cadastrado com sucesso:", usuario);
            alert("Cadastro realizado com sucesso!");

            localStorage.setItem(
                "usuario",
                JSON.stringify(usuario)
            );

            // Troca para a aba de login
            switchTab('login');
        });
    }
});


//Recuperar o usuario na function de logar
/*const usuario = JSON.parse(
    localStorage.getItem("usuario")
);

console.log(usuario);*/

const logar = document.getElementById("logar");

//Function para logar no sistema
document.addEventListener("submit", function(event){

    event.preventDefault();

    //Usuario no localstorage
    const usuario = JSON.parse(
        localStorage.getItem("usuario")
    );
    
    console.log(usuario);

    const loginSenha = document.getElementById("login-password").value;
    const loginEmail = document.getElementById("login-email").value;

    if(loginEmail !== usuario.email){

        console.log("Email incorreto");
        return alert("Email incorreto");
    }
    
    if(loginSenha !== usuario.senha){
        console.log("Senha incorreto");
        return alert("Senha incorreto");
    }

    console.log("Login realizado com sucesso"); 

    window.location.href = "http://127.0.0.1:5500/src/main/resources/html/index.html";
})