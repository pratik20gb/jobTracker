import { useEffect, useState } from "react";
import "./App.css";

function ApplicationCard({ application }) {
  return (
    <article className="application-card">
      <div className="company-icon">
        {application.company.charAt(0)}
      </div>

      <div className="application-details">
        <h2>{application.company}</h2>
        <p>{application.role}</p>
      </div>

      <span
        className={`status status-${application.status.toLowerCase()}`}
      >
        {application.status}
      </span>
    </article>
  );
}

export default function App() {
  const [applications, setApplications] = useState([]);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    async function loadApplications() {
      try {
        const response = await fetch(
          "/api/applications?page=0&size=10",
          { signal: controller.signal }
        );

        if (!response.ok) {
          throw new Error(
            `Could not load applications (HTTP ${response.status}).`
          );
        }

        const data = await response.json();

        if (!controller.signal.aborted) {
          setApplications(data.content);
          setTotalElements(data.totalElements);
        }
      } catch (error) {
        if (!controller.signal.aborted) {
          setError(error.message || "Could not load applications.");
        }
      } finally {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      }
    }

    loadApplications();

    return () => controller.abort();
  }, []);

  return (
    <main className="dashboard">
      <header className="page-header">
        <p className="eyebrow">JOBTRACKER</p>
        <h1>Your next chapter.</h1>
        <p className="subtitle">
          Keep your applications organized, one opportunity at a time.
        </p>
      </header>

      <section aria-labelledby="applications-heading">
        <div className="section-heading">
          <h2 id="applications-heading">Applications</h2>

          {!loading && !error && (
            <span>
              Showing {applications.length} of {totalElements}
            </span>
          )}
        </div>

        {loading && (
          <p className="feedback" role="status">
            Loading applications…
          </p>
        )}

        {!loading && error && (
          <div className="feedback feedback-error" role="alert">
            <p>{error}</p>
            <p>Check that the backend is running, then reload this page.</p>
          </div>
        )}

        {!loading && !error && applications.length === 0 && (
          <p className="feedback">
            No applications yet. Add one using Postman for now,
            then reload this page.
          </p>
        )}

        {!loading && !error && applications.length > 0 && (
          <div className="application-list">
            {applications.map((application) => (
              <ApplicationCard
                key={application.id}
                application={application}
              />
            ))}
          </div>
        )}
      </section>
    </main>
  );
}