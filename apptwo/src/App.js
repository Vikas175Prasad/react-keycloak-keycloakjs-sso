import "./App.css";
import React, { useState, useEffect } from "react";
import keycloak from "./keycloak";

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "check-sso",
        pkceMethod: "S256",
        silentCheckSsoRedirectUri:
          window.location.origin + "/silent-check-sso.html",
      })
      .then(async (auth) => {
        setAuthenticated(auth);
        setReady(true);
        if (auth) {
          fetchProtectedAPI();
        }
      })
      .catch((err) => {
        console.error("Keycloak init failed", err);
      });
  }, []);

  const fetchProtectedAPI = async () => {
    try {
      const response = await fetch("http://localhost:9091/api/data", {
        headers: {
          Authorization: `Bearer ${keycloak.token}`,
        },
      });
      if (!response.ok) {
        throw new Error("API request failed");
      }
      const data = await response.text;
      console.log(data);
    } catch (err) {
      console.error("Error calling API:", err);
    }
  };

  const handleLogin = () => {
    keycloak.login().then((res) => {
      console.log(res);
    });
  };

  const handleLogout = () => {
    keycloak.logout({ redirectUri: window.location.origin });
  };

  setInterval(() => {
    keycloak.updateToken(60).then((refreshed) => {
      if (refreshed) {
        console.log("Token refreshed");
      }
    });
  }, 60000);

  if (!ready) return <>Loading....</>;
  return (
    <div>
      {authenticated ? (
        <>
          {" "}
          <h2>Welcome, {keycloak.tokenParsed?.preferred_username}</h2>
          <button onClick={handleLogout}>Logout</button>
        </>
      ) : (
        <p>
          {/* Loading..... */}
          <button onClick={handleLogin}>Login</button>
        </p>
      )}
    </div>
  );
}

export default App;
