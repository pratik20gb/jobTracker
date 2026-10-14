import { useState } from "react";

const statuses = [
  "SAVED",
  "APPLIED",
  "INTERVIEW",
  "OFFER",
  "REJECTED",
];

export default function ApplicationCard({ application, onChanged }) {
  const [pending, setPending] = useState(false);
  const [error, setError] = useState("");

  async function updateStatus(status) {
    if (status === application.status) return;

    setPending(true);
    setError("");

    try {
      const response = await fetch(
        `/api/applications/${application.id}/status`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ status }),
        }
      );

      if (!response.ok) {
        throw new Error(
          `Could not update status (HTTP ${response.status}).`
        );
      }

      onChanged("Status updated successfully.");
    } catch (error) {
      setError(error.message || "Could not update status.");
    } finally {
      setPending(false);
    }
  }

  async function deleteApplication() {
    const confirmed = window.confirm(
      `Delete the ${application.role} application at ${application.company}?`
    );

    if (!confirmed) return;

    setPending(true);
    setError("");

    try {
      const response = await fetch(
        `/api/applications/${application.id}`,
        { method: "DELETE" }
      );

      if (!response.ok) {
        throw new Error(
          `Could not delete application (HTTP ${response.status}).`
        );
      }

      onChanged("Application deleted successfully.");
    } catch (error) {
      setError(error.message || "Could not delete application.");
    } finally {
      setPending(false);
    }
  }

  return (
    <article className="application-card">
      <div className="company-icon">
        {application.company.charAt(0)}
      </div>

      <div className="application-details">
        <h2>{application.company}</h2>
        <p>{application.role}</p>
      </div>

      <div className="card-actions">
        <label
          className="sr-only"
          htmlFor={`status-${application.id}`}
        >
          Status for {application.role} at {application.company}
        </label>

        <select
          id={`status-${application.id}`}
          className="status-select"
          value={application.status}
          onChange={(event) => updateStatus(event.target.value)}
          disabled={pending}
        >
          {statuses.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>

        <button
          type="button"
          className="delete-button"
          onClick={deleteApplication}
          disabled={pending}
          aria-label={`Delete ${application.role} at ${application.company}`}
        >
          Delete
        </button>
      </div>

      {pending && (
        <p className="card-message" role="status">
          Saving changes…
        </p>
      )}

      {error && (
        <p className="card-message form-error" role="alert">
          {error}
        </p>
      )}
    </article>
  );
}