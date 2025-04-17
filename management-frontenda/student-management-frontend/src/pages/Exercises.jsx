import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/exercises.css";

export default function Exercises() {
  const [exercises, setExercises] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const storedExercises = localStorage.getItem("exercises");
    if (storedExercises) {
      setExercises(JSON.parse(storedExercises));
    }
  }, []);

  return (
    <div className="wrapper">
      <div className="exercises-container page-container">
        <h2 className="title">Exercícios Gerados</h2>

        {exercises.length === 0 ? (
          <p>Nenhum exercício foi gerado.</p>
        ) : (
          <ul className="exercise-list">
            {exercises.map((exercise) => (
              <li key={exercise.id} className="exercise-item">
                {exercise.description}
              </li>
            ))}
          </ul>
        )}

        <button className="back-button" onClick={() => navigate("/generator")}>
          Voltar
        </button>
      </div>
    </div>
  );
}
