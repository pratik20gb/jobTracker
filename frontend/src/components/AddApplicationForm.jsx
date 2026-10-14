import { useState } from "react";

export default function AddApplicationForm({ onCreated }) {
  const [company, setCompany] = useState("");
  const [role, setRole] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();

    const trimmedCompany = company.trim();
    const trimmedRole = role.trim();

    if (!trimmedCompany || !trimmedRole) {
      setError("Company and role must not be blank.");
      return;
    }

    setSubmitting(true);
    setError("");

    try {
      const response = await fetch("/api/applications", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          company: trimmedCompany,
          role: trimmedRole,
        }),
      });

      if (!response.ok) {
        throw new Error(
          response.status === 400
            ? "Check your inputs. Company and role must be 1–100 characters."
            : `Could not save application (HTTP ${response.status}).`
        );
      }

      setCompany("");
      setRole("");
      onCreated();
    } catch (error) {
      setError(error.message || "Could not save application.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="add-application" aria-labelledby="add-heading">
      <h2 id="add-heading">Add an application</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-fields">
          <div className="form-field">
            <label htmlFor="company">Company</label>
            <input
              id="company"
              name="company"
              value={company}
              onChange={(event) => setCompany(event.target.value)}
              placeholder="e.g. Atlassian"
              maxLength={100}
              required
              disabled={submitting}
            />
          </div>

          <div className="form-field">
            <label htmlFor="role">Role</label>
            <input
              id="role"
              name="role"
              value={role}
              onChange={(event) => setRole(event.target.value)}
              placeholder="e.g. Java Developer"
              maxLength={100}
              required
              disabled={submitting}
            />
          </div>

          <button type="submit" disabled={submitting}>
            {submitting ? "Saving…" : "Add application"}
          </button>
        </div>

        {error && (
          <p className="form-error" role="alert">
            {error}
          </p>
        )}
      </form>
    </section>
  );
}