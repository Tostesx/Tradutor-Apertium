# 📘 Tradutor PT → ES com Apertium (https://www.apertium.org)

Este projeto realiza a tradução de textos do **Português para o Espanhol** utilizando a ferramenta **Apertium**, uma plataforma de tradução automática de código aberto.

---

## 🚀 Funcionalidades

- Tradução automática de Português (PT) para Espanhol (ES)
- Uso do Apertium como motor de tradução
- Interface/script simples para rodar via terminal ou aplicação
- Código limpo e organizado em uma única pasta (sem dependências externas de código)

---
## ☕ Requisitos

Java **21** LTS e 
**(WLS)** para o Windows

## ⚠️ Dependência Externa Obrigatória

Este projeto **não inclui o Apertium no repositório**.

✅ **Você precisa instalá-lo no seu sistema antes de usar o tradutor**, senão o script não irá funcionar.

---

## 💻 Instalação do Apertium

###🪟 **Windows (via WSL)**
 Instale o WSL (Subsistema Windows para Linux) e o Ubuntu:

```bash
wsl --install -d Ubuntu
```
```bash
sudo apt update
sudo apt install apertium apertium-por-spa
```

### 🐧 **Linux (Ubuntu / Debian / Mint / etc.)**

```bash
sudo apt update
sudo apt install apertium apertium-por-spa
```
