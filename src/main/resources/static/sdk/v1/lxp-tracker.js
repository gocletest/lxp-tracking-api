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
      if (!API_KEY) {
        console.warn("[LXP] API key missing");
        return;
      }
      const event = buildEvent(eventType, payload);
      send(event);
    }
  };

  function buildEvent(eventType, payload) {
    return {
      apiKey: API_KEY,
      eventType: String(eventType).toUpperCase(),
      timestamp: new Date().toISOString(),
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
    const url = API_KEY
      ? `${ENDPOINT}?apiKey=${encodeURIComponent(API_KEY)}`
      : ENDPOINT;

    try {
      navigator.sendBeacon(url, JSON.stringify(event));
    } catch (e) {
      fetch(url, {
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
