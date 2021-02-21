import os

from dataclasses import dataclass
from google.cloud import secretmanager


client = secretmanager.SecretManagerServiceClient()
app_id = os.getenv('GOOGLE_CLOUD_PROJECT')

@dataclass(frozen=True)
class Secret:
  name: str

  def get(self):
    # Build the resource name of the secret version.
    name = f"projects/{app_id}/secrets/{self.name}/versions/latest"

    response = client.access_secret_version(request={"name": name})
    payload = response.payload.data.decode("UTF-8")
    return payload
