import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: 'http://localhost:8080',
    realm: 'reactsso',
    clientId: 'keycloakjs-client'
});

export default keycloak;