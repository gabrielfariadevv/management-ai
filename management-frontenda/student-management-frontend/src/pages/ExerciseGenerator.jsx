import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/exerciseGenerator.css";

export default function ExerciseGenerator() {
  const [subject, setSubject] = useState("");
  const [topic, setTopic] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [difficulty, setDifficulty] = useState("");
  const [exercises, setExercises] = useState(null);
  const [error, setError] = useState(""); // Para mostrar erros
  const navigate = useNavigate();

  const handleGenerateClick = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/exercises/generate",
        {
          subject,
          topic,
          quantity,
          difficulty,
        }
      );

      const exercisesList = response.data.description
        .split("\n")
        .map((exercise, index) => ({
          id: index + 1,
          description: exercise,
        }));

      setExercises(exercisesList);
      navigate("/exercises");
    } catch (error) {
      setError("Erro ao gerar os exercícios. Tente novamente!");
      console.error("Erro ao gerar exercícios:", error);
    }
  };

  return (
    <div className="wrapper">
      <div className="exercise-container page-container">
        <h2 className="title">Gerador de Exercícios</h2>
        <button className="back-button" onClick={() => navigate("/")}>
          Voltar para Home
        </button>
        <div className="input-container">
          <label>Qual matéria você quer estudar?</label>
          <select value={subject} onChange={(e) => setSubject(e.target.value)}>
            <option value="">Selecione a matéria</option>
            <option value="matematica">Matemática</option>
            <option value="portugues">Português</option>
            <option value="fisica">Física</option>
            <option value="quimica">Química</option>
            <option value="biologia">Biologia</option>
            <option value="historia">História</option>
            <option value="geografia">Geografia</option>
            <option value="sociologia">Sociologia</option>
            <option value="filosofia">Filosofia</option>
            <option value="ingles">Inglês</option>
          </select>
        </div>
        <div className="input-container">
          <label>Qual assunto você quer estudar?</label>
          <input
            type="text"
            value={topic}
            onChange={(e) => setTopic(e.target.value)}
            placeholder="Ex: Equações do 2º grau"
          />
        </div>
        <div className="input-container">
          <label>Quantos exercícios você quer fazer?</label>
          <input
            type="number"
            min="1"
            value={quantity}
            onChange={(e) => setQuantity(Number(e.target.value))}
          />
        </div>
        <div className="input-container">
          <label>Qual a dificuldade?</label>
          <select
            value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}
          >
            <option value="">Selecione a dificuldade</option>
            <option value="facil">Fácil</option>
            <option value="medio">Médio</option>
            <option value="dificil">Difícil</option>
          </select>
        </div>
        <button className="generate-button" onClick={handleGenerateClick}>
          Gerar
        </button>
        {error && <p className="error-message">{error}</p>}{" "}
        {/* Mensagem de erro */}
        {exercises && (
          <div className="exercise-results">
            <h3>Exercícios gerados:</h3>
            <ul>
              {exercises.map((ex) => (
                <li key={ex.id}>{ex.description}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
