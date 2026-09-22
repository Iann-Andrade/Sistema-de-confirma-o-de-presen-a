// Executado assim que a página index.html carrega
window.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');

    // Se não existir token salvo, expulsa o usuário para a tela de login
    if (!token) {
        window.location.href = '/cadastro.html';
    }
});

//contstante endereço do site
const API_BASE_URL = "https://pelafeu.onrender.com";

//Salva o valor da data
let dataSelecionada = null;

 // ---------- INICIALIZA O FULLCALENDAR ----------
 document.addEventListener('DOMContentLoaded', function() {
     const calendarEl = document.getElementById('calendar');
     const calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'pt-br',
        initialView: 'dayGridMonth',
        height: 'auto',
        fixedWeekCount: false,
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: '',
        },
      
        // 1. Habilita a seleção de datas
        selectable: true,
      
        // 2. Captura o clique no dia
        dateClick: function(info) {
            dataSelecionada = info.dateStr;  
            console.log('Data clicada:', dataSelecionada);
            buscarAgendamentos(dataSelecionada)
        }
      });
      
      calendar.render();
  });

//Function de buscar os agendamentos do dia
/*function buscarAgendamentos(dataSelecionada){

        try{
            const response = await fetch(`${API_BASE_URL}/agendamento/buscar?data=${dataSelecionada}`,{
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            const listaHorarios = await response.json;

            renderizarHoraiosAgendamentos(listaHorarios);

        }catch(erro){
            console.error("Erro ao buscar agendamentos:", erro);
        }
};*/

function abrirModal() {
    const modal = document.getElementById("card-criar-agendamento");
    const dataSelect = document.getElementById("data-selecionada")
    
    if(dataSelecionada == null){
        window.alert("Selecione a data para dar continuidade!")
        return;
    }

    dataSelect.textContent = dataSelecionada;
    
    modal.showModal(); 
    
}

//criar agendamento
document.addEventListener("DOMContentLoaded", function () {
    const formAgendamento = document.getElementById("form-agendamento");
    const fecharModal = document.getElementById("fechar-modal");
    const modal = document.getElementById("card-criar-agendamento");
    const btnSalvar = document.getElementById("salvarAgendamento");

    if (fecharModal && modal) {
        fecharModal.addEventListener("click", function (event) {
            event.preventDefault();
            formAgendamento.reset();
            modal.close();          
        });
    };

    if (btnSalvar && formAgendamento) {
        btnSalvar.addEventListener("click", async function (event) {
            event.preventDefault(); 

            const token = localStorage.getItem("token");
            const formData = new FormData(formAgendamento);
            const agendamento = Object.fromEntries(formData.entries());

            const spanData = document.getElementById("data-selecionada");

            if (spanData && spanData.textContent.trim() !== "") {
                agendamento.data = spanData.textContent.trim();
            } else {
                alert("Selecione uma data válida antes de prosseguir.");
                return; 
            }

            const temCampoVazio = Object.values(agendamento).some(
                valor => valor === null || valor === undefined || valor.trim() === ""
            );
            
            if (temCampoVazio) {
                alert("Preencha todos os campos.");
                return;
            }

            try {
                const response = await fetch(`${API_BASE_URL}/agendamento/criar`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                    },
                    body: JSON.stringify(agendamento)
                });

                if (!response.ok) {
                    alert("Erro ao criar agendamento!");
                    return;
                }

                const retorno = await response.json();

                if (modal) modal.close();
                formAgendamento.reset();

                if (typeof renderizarHoraiosAgendamentos === "function") {
                    console.log("RETORNO:", retorno);
                    console.log("É array?", Array.isArray(retorno));

                    buscarAgendamentos();
                }

            } catch (error) {
                console.error("Erro no envio:", error);
            }
        });
    } else {
        console.error("Erro: O formulário id='form-agendamento' não foi encontrado no HTML.");
    }
});

//Buscar agendaementos
async function buscarAgendamentos(){
    const token = localStorage.getItem("token");

   // O try encapsula a execução assíncrona que pode falhar
   try {
    const response = await fetch(`${API_BASE_URL}/agendamento?data=${dataSelecionada}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        if (response.status === 403) {
            alert("Sessão expirada ou sem permissão. Faça login novamente.");
        }
        // Lança o erro para interromper e cair direto no catch
        throw new Error(`Erro na requisição: ${response.status}`);
    }

    const agendamentos = await response.json();
    console.log("Retorno dos agendamentos:", agendamentos);
    renderizarHoraiosAgendamentos(agendamentos);

} catch (erro) {
    console.error("Falha ao carregar agendamentos:", erro.message);
    
    const container = document.getElementById("eventList");
    container.innerHTML = `
        <div class="erro-agendamento">
            <span>Não foi possível carregar os agendamentos no momento.</span>
        </div>
    `;
    }
};

//Renderizar agendamentos
function renderizarHoraiosAgendamentos(agendamentos){

    const container = document.getElementById("eventList");
    container.innerHTML = "";

    // Verificação para caso o retorno seja um array vazio
    if (!agendamentos || agendamentos.length === 0) {
        const nulo = document.createElement("div");
        nulo.innerHTML = `
            <div>
                <span>Nenhum agendamento encontrado</span>
            </div>
        `;
        container.appendChild(nulo);
        return;
    };

    agendamentos.forEach(item => {
        const card = document.createElement("div");
        card.className = "card-agendamento";

        card.innerHTML = `
            <div class="agendamento">
                <div class= "card-topo-agendamento">
                    <div class="ifo-agendamento">
                        <h1>${item.nome}</h1>
                        <h2>${item.descricao}</h2>
                        <p>Horario: ${item.horaInicio} - ${item.horaFim}</p>
                    </div>
                    <div class="config-agendamento">
                        <button>
                            <span class="material-icons">edit</span>
                        </button>
                        <button class="btn-excluir">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                    <div class="data-agendamento">
                        <span>${item.data}</span>
                    </div>
                </div>

                    <div class="container-confirmar">
                        <div class="confirmados-agendamentos">
                            <button class="btn-lista" data-id="${item.id}">Lista de confirmados</button>

                            <dialog class="card-lista-confirmados">
                                <div class="header-lista">
                                    <h1>Lista de confirmados</h1>
                                    <button class="fechar-lista" aria-label="Fechar">
                                        &times;
                                    </button>
                                </div>
                                <div class="card-lista">
                                    <ul class="lista-presenca">
                                        
                                    </ul>
                                </div>
                            </dialog>
                        </div>
                        <div class="confirmar-presenca">
                            <button class="btn-confirmar" id="confirm-btn" data-id="${item.id}">Confirmar</button>
                        </div>
                    </div>
            </div>
      `;

        const estaConfirmado = item.confirmado === true | item.isConfirmado === true;
      
        if(estaConfirmado){
            const btnConfirmar = card.querySelector(".btn-confirmar");
            btnConfirmar.innerText = "Presença confirmada";
            btnConfirmar.disabled = true;
        }

      container.appendChild(card);
    })
    
};

//Confirmar presença de alunos em eventos 
document.addEventListener("click", async function(event) {

    const token = localStorage.getItem("token");
    console.log("CLIQUE DETECTADO", event.target);

        if (event.target.classList.contains("btn-confirmar")) {
    
            const agendamentoId = event.target.dataset.id;
            const btnClicado = event.target;
            console.log("ID do agendamento:", agendamentoId);
        

            const response = await fetch(`${API_BASE_URL}/presenca/${agendamentoId}/confirmar`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

            if(!response.ok){
                alert("Erro ao confirmar presença");
                return;
            };

            if(response.ok){
                buscarAgendamentos(dataSelecionada);
            }
        }
    
    });



document.addEventListener("click", async function renderizarListaPresenca(event) {
    

    const token = localStorage.getItem("token");

    if(event.target.classList.contains("btn-lista")){
        
        const agendamentoId = event.target.dataset.id;

        const response = await fetch(`${API_BASE_URL}/presenca/${agendamentoId}/confirmados`,{
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if(!response.ok){
            alert("Erro na busca de presensas confirmadas, tente novamente!")
            return;
        }

        const nomes = await response.json();
        
        const card = document.querySelector(".lista-presenca");
        
        card.innerHTML ="";

        nomes.forEach(nome => { 
            console.log(nome);
            const li = document.createElement("li");
            li.innerHTML = "";
            
            li.innerHTML = `
                <span>${nome}</span>
            `

            card.appendChild(li);
        });
        mostrarDialogLista();
    }

})


function mostrarDialogLista() {

    const dialog = document.querySelector(".card-lista-confirmados");

    dialog.showModal();
}

document.addEventListener("click", function(event) {

    const botaoFechar = event.target.closest(".fechar-lista");

    if (botaoFechar) {
        const dialog = document.querySelector(".card-lista-confirmados");

        dialog.close();
    }

});





