import re

from dataclasses import is_dataclass, asdict
from google.cloud import datastore


def _to_snake_case(string):
  return re.sub(r'(?<!^)(?=[A-Z])', '_', string).lower()

def fetch_last(model_type):
  type_str = model_type.__name__
  if not is_dataclass(model_type):
    raise Exception(f'{type_str} is not a @dataclass')

  client = datastore.Client()
  model_key = client.key(type_str, f'{_to_snake_case(type_str)}.last')

  model = client.get(model_key)
  if not model:
    return None
  return model_type(**model)

def set_last(model):
  model_type = type(model)
  type_str = model_type.__name__
  if not is_dataclass(model_type):
    raise Exception(f'{type_str} is not a @dataclass')

  client = datastore.Client()
  model_key = client.key(type_str, f'{_to_snake_case(type_str)}.last')

  entity = datastore.Entity(key=model_key)
  entity.update(asdict(model))
  client.put(entity)
