package fr.insee.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.vault.VaultNotFoundException;
import org.keycloak.vault.VaultProvider;
import org.keycloak.vault.VaultProviderFactory;

import fr.insee.vault.VaultService;

public class HashicorpVaultProviderFactory implements VaultProviderFactory {
   private static final Logger logger = Logger.getLogger(HashicorpVaultProviderFactory.class);

   public static final String PROVIDER_ID = "hachicorp-vault";

   private String vaultToken;
   private String vaultUrl;

   @Override
   public VaultProvider create(KeycloakSession session) {
      VaultService service = new VaultService(session);
      if(!service.isVaultAvailable(vaultUrl, vaultToken)){
         logger.error("Vault unavailable : "+ vaultUrl);
         throw new VaultNotFoundException("Vault unavailable : "+ vaultUrl);
      }else{
         logger.info("Vault available : "+ vaultUrl);
      }
      return new HashicorpVaultProvider(vaultUrl, vaultToken,  session.getContext().getRealm().getName(), service);

   }

   @Override
   public void init(Scope config) {
      vaultToken = config.get("token");
      vaultUrl = config.get("url");
      logger.info("Init Hashicorp: "+ vaultUrl);
   }

   @Override
   public void postInit(KeycloakSessionFactory factory) {
      // TODO Auto-generated method stub

   }

   @Override
   public void close() {
      // TODO Auto-generated method stub

   }

   @Override
   public String getId() {
      return PROVIDER_ID;
   }
   

   
}