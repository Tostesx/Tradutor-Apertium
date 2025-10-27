package main.tostesx.com.github.tradutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class TradutorApertium {
    
    private static final int TIMEOUT_SEGUNDOS = 30; // Tempo de espera caso trave
    
    public static boolean testarApertium() {
        try {
            Process process = new ProcessBuilder("wsl", "apertium", "--version").start();
            return process.waitFor(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS) && 
                   process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String traduzir(String texto, String direcao) throws IOException {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        String textoProcessado = preProcessarTexto(texto);
        Process process = null;
        
        try {
            // Usando a abordagem comprovada que funciona no Git Bash
            String comando = String.format("apertium %s <<< '%s'", 
                direcao, 
                textoProcessado.replace("'", "'\\''")); // Escapa aspas simples corretamente
            
            process = new ProcessBuilder("wsl", "bash", "-c", comando)
                .redirectErrorStream(true)
                .start();
            
            // Lendo tanto a saída padrão quanto de erro
            StringBuilder resultado = new StringBuilder();
            StringBuilder erros = new StringBuilder();
            
            try (BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                
                if (!process.waitFor(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                    throw new IOException("Timeout: A tradução excedeu o tempo limite de " + 
                                         TIMEOUT_SEGUNDOS + " segundos");
                }
                
                // Lê a saída padrão
                String linha;
                while ((linha = outputReader.readLine()) != null) {
                    resultado.append(linha).append("\n");
                }
                
                // Lê a saída de erro
                while ((linha = errorReader.readLine()) != null) {
                    erros.append(linha).append("\n");
                }
            }
            
            // Verifica se houve erros
            if (erros.length() > 0) {
                throw new IOException("Erro no Apertium: " + erros.toString().trim());
            }
            
            // Remove última quebra de linha se existir
            if (resultado.length() > 0) {
                resultado.setLength(resultado.length() - 1);
            }
            
            return resultado.toString();
            
        } catch (IOException | InterruptedException e) {
            throw new IOException("Erro na tradução: " + e.getMessage(), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
    
    private static String preProcessarTexto(String texto) {
        // Remove quebras de linha múltiplas e espaços extras
        String processado = texto.trim()
            .replace("\"", "\\\"")
            .replace("\n", " ")
            .replace("\r", "")
            .replace("?", " ?")
            .replace("!", " !")
            .replace(".", " .")
            .replace(",", " ,")
            .replace(";", " ;");
            
        // Remove múltiplos espaços consecutivos
        return processado.replaceAll("\\s+", " ");
    }
}