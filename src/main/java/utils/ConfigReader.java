package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utilitário para ler propriedades de um arquivo de configuração (config.properties).
 * Tenta carregar o arquivo primeiro do classpath e, como fallback, do sistema de arquivos.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_NAME = "config.properties";

    static {
        boolean loaded = false;
        // Tenta carregar do classpath primeiro
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input != null) {
                properties.load(input);
                System.out.println("'" + CONFIG_FILE_NAME + "' carregado com sucesso do Classpath.");
                loaded = true;
            } else {
                System.out.println("Não foi possível encontrar '" + CONFIG_FILE_NAME + "' no classpath. Tentando via FileSystem...");
            }
        } catch (IOException ex) {
            System.err.println("ERRO ao carregar '" + CONFIG_FILE_NAME + "' do Classpath: " + ex.getMessage());
        }

        // Fallback para o sistema de arquivos se não carregou do classpath
        if (!loaded) {
            try (InputStream fileInput = new FileInputStream("src/test/resources/" + CONFIG_FILE_NAME)) {
                properties.load(fileInput);
                System.out.println("'" + CONFIG_FILE_NAME + "' carregado com sucesso do FileSystem.");
                loaded = true; // Define como carregado aqui também
            } catch (IOException ex) {
                System.err.println("ERRO ao carregar '" + CONFIG_FILE_NAME + "' do FileSystem: " + ex.getMessage());
            }
        }

        if (!loaded && properties.isEmpty()){ // Verifica se realmente não carregou de nenhuma fonte
            throw new RuntimeException("FALHA CRÍTICA: Não foi possível carregar o arquivo de configuração '" + CONFIG_FILE_NAME + "' de nenhuma fonte.");
        }
    }

    /**
     * Obtém o valor de uma propriedade como String.
     * @param key A chave da propriedade.
     * @return O valor da propriedade, ou null se a chave não for encontrada.
     */
    public static String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            return property.trim();
        }
        return null; // Não logar erro aqui, deixar métodos com defaultValue tratarem
    }

    /**
     * Obtém o valor de uma propriedade como String, com um valor padrão.
     * @param key A chave da propriedade.
     * @param defaultValue O valor padrão a ser retornado se a chave não for encontrada.
     * @return O valor da propriedade, ou o valor padrão.
     */
    public static String getProperty(String key, String defaultValue) {
        String property = properties.getProperty(key, defaultValue);
        if (property != null) {
            return property.trim();
        }
        // Se property é null, significa que defaultValue também era null e a chave não existe.
        // O properties.getProperty(key, defaultValue) já retorna defaultValue se key não existe.
        // A mensagem de aviso é mais útil se o defaultValue original for usado.
        System.out.println("AVISO: Propriedade '" + key + "' não encontrada. Valor padrão ('" + defaultValue + "') será usado.");
        return defaultValue;
    }

    /**
     * Obtém o valor de uma propriedade como int, com um valor padrão.
     * @param key A chave da propriedade.
     * @param defaultValue O valor padrão a ser retornado se a chave não for encontrada ou o valor não for um inteiro.
     * @return O valor da propriedade como int, ou o valor padrão.
     */
    public static int getIntProperty(String key, int defaultValue) {
        String propertyValue = getProperty(key);
        if (propertyValue != null) {
            try {
                return Integer.parseInt(propertyValue);
            } catch (NumberFormatException e) {
                System.err.println("ERRO: Valor da propriedade '" + key + "' ('" + propertyValue + "') não é um inteiro válido. Usando valor padrão: " + defaultValue);
                return defaultValue;
            }
        }
        System.out.println("AVISO: Propriedade '" + key + "' não encontrada para inteiro. Usando valor padrão: " + defaultValue + ".");
        return defaultValue;
    }

    /**
     * Obtém o valor de uma propriedade como boolean, com um valor padrão.
     * @param key A chave da propriedade.
     * @param defaultValue O valor padrão a ser retornado se a chave não for encontrada.
     * @return O valor da propriedade como boolean (interpretado por Boolean.parseBoolean), ou o valor padrão.
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String propertyValue = getProperty(key);
        if (propertyValue != null) {
            return Boolean.parseBoolean(propertyValue);
        }
        System.out.println("AVISO: Propriedade '" + key + "' não encontrada para booleano. Usando valor padrão: " + defaultValue + ".");
        return defaultValue;
    }
}