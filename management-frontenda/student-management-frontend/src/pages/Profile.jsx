import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Profile.css";

export default function Profile() {
  const navigate = useNavigate();

  return (
    <div className="wrapper">
      <div className="profile-container">
        <h2 className="profile-title">Perfil de João</h2>

        <button className="back-button" onClick={() => navigate("/")}>
          Voltar para Home
        </button>

        <div className="profile-info">
          <p>
            <strong>Nome:</strong> João Vitor
          </p>
          <p>
            <strong>Email:</strong> joao@example.com
          </p>
          <p>
            <strong>Exercícios feitos:</strong> 15
          </p>
          <p>
            <strong>Progresso semanal:</strong> 5 de 7 dias
          </p>
          <p>
            <strong>Meta de estudos:</strong> Estudar 20 minutos por vez
          </p>
        </div>

        <div className="profile-actions">
          <button className="edit-profile-button">Editar Perfil</button>
        </div>
      </div>
    </div>
  );
}
