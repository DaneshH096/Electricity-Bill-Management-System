document.addEventListener("DOMContentLoaded", function () {
  const params = new URLSearchParams(window.location.search);
  const message = params.get("message");
  const status = params.get("status");

  if (message) {
    const popup = document.createElement("div");
    popup.className = `popup ${status === "success" ? "success" : "error"}`;
    popup.textContent = message;
    document.body.appendChild(popup);

    popup.style.display = "block";

    setTimeout(() => {
      popup.remove();
    }, 3000);
  }
});
