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

/* Garantir que o DOM está carregado antes de manipular os elementos
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
});*/

//Function de cadastrar novo usuario
async function cadastrar(event) {

    if(event) event.preventDefault();
    
    let nome = document.getElementById("register-name").value;
    let email = document.getElementById("register-email").value;
    let senha = document.getElementById("register-password").value;
    let senhaConfirm = document.getElementById("confirm-password").value;

    if(!nome || !email || !senha || !senhaConfirm){
       event.preventDefault();
        window.alert("Preencha todos os campos para seguir");
        return;
    }

    const response = await fetch("http://localhost:8080/usuarios/cadastrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            nome,
            email,
            senha,
            senhaConfirm
        })
    });

    const resposta = await response.json();

    if(!response.ok){
        alert("Não foi possível realizar o cadastro, por favor tente novamente!");
        return;
    }

    // Troca para a aba de login
    switchTab('login');

}


//Function de logar usuario
async function login(event) {

    if(event) event.preventDefault();
    
    const email = document.getElementById("login-email").value;
    const senha = document.getElementById("login-password").value;

    try{

        const response = await fetch("http://localhost:8080/usuarios/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                senha: senha
            })
        });
        
        if(!response.ok){
            if (response.status === 401) {
                alert("E-mail ou senha incorretos!");
                return;
            }
            throw new Error(`Erro na requisição: ${response.status}`);
        }
        
        const dados = await response.json();
        
        localStorage.setItem("token", dados.token);
        
        window.location.href = "index.html";    
    }catch(error){
        console.error("Falha ao conectar com o servidor:", error);
        alert("Não foi possível conectar ao servidor. Verifique se o backend está rodando.");
    }

};