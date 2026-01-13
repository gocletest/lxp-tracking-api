(function (window, document) {

  const SCRIPT = document.currentScript;

  const API_KEY = SCRIPT?.getAttribute("data-lxp-key");
  const ENDPOINT = SCRIPT?.getAttribute("data-lxp-endpoint");

  if (!ENDPOINT) {
    console.warn("[LXP] endpoint not found");
    return;
  }

  const LXP = {
    version: "1.0.0",

    track(eventType, payload = {}) {
      const event = buildEvent(eventType, payload);
      send(event);
    }
  };

  function buildEvent(eventType, payload) {
    return {
      eventType,
      timestamp: new Date().toISOString(),
      apiKey: API_KEY,
      user: {
        id: payload.userId || getUserId()
      },
      target: {
        id: payload.courseId || payload.contentId || "UNKNOWN"
      },
      context: {
        platform: window.location.hostname,
        userAgent: navigator.userAgent
      },
      raw: payload
    };
  }

  function send(event) {
    try {
      navigator.sendBeacon(ENDPOINT, JSON.stringify(event));
    } catch (e) {
      fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(event)
      }).catch(() => {});
    }
  }

  function getUserId() {
    return (
      window.LXP_USER_ID ||
      localStorage.getItem("LXP_USER_ID") ||
      "ANONYMOUS"
    );
  }

  window.LXP = LXP;

})(window, document);
