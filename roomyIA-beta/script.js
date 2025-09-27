const userInput = document.getElementById("user-input");
const chatBox = document.getElementById("chat-box");

function addMessage(sender, text) {
  const message = document.createElement("div");
  message.className = `message ${sender}`;
  message.innerText = text;
  chatBox.appendChild(message);
  chatBox.scrollTop = chatBox.scrollHeight;
}

async function sendMessage() {
  const input = userInput.value.trim();
  if (!input) return;

  addMessage("user", input);
  userInput.value = "";

  try {
    const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
      method: "POST",
      headers: {
        "Authorization": "Bearer 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", // API Key
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        model: "openai/gpt-oss-120b",
        messages: [
          {
            role: "system",
            content: "Eres Roomy, un asistente médico empático que responde en español con orientación clara y respetuosa. Puedes sugerir tratamientos básicos cuando los sintomas no son alarmantes o graves. No diagnosticas ni recetas. Tu función es orientar sobre síntomas comunes, autocuidado y cuándo acudir a un centro médico."
          },
          {
            role: "user",
            content: input
          }
        ]
      })
    });

    const data = await response.json();
    const reply = data.choices?.[0]?.message?.content || "Lo siento, no pude procesar tu mensaje.";
    addMessage("bot", reply);
  } catch (error) {
    console.error("Error al conectar con Medibot:", error);
    addMessage("bot", "⚠️ Error de conexión con Medibot. Intenta más tarde.");
  }
}

