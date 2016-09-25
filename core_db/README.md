## Core database
Stores information about food, sources, distances, has web interface for editing, REST API.

## Delpoy
Is prepared with ansible, go to ansible dir, change hosts file and execute: 
```
ansible-playbook -i hosts inventory.yml -v
```

### Development on MacOs X hints
 
```
brew install spatialite-tools
brew install gdal
```
