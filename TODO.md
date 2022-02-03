# TODO
- [ ] Finish implementation of InstanceManager
- [ ] ... and link it to Anura
- [ ] Implement viable time system
## Instance
- [ ] Serializable properties file for guild info
  - [ ] Store in SQL
## Modules
- [ ] Submission module
  - [ ] Upsert commands per guild
    - [ ] Allow manual activation / deactivation of certain commands
- [ ] Timer / Event module
- [ ] Manual request module
- [ ] Invite module (for new guilds)
  - Query the guild owner for all necessary information
    - Language
    - TimeZone / Offset
      - Maybe a *What's the time at your location?* with a few presets (UTC, MESZ, ...) and an option to manually set the offset (*show what the current time **would** be with the current offset while choosing*)
    - Modules (Which ones should be activated?)
    - Commands (Which ones should be usable?)
## Data Service
- [ ] Introduce meme retrieval
  - see discord chat for details on implementation of 'randomized' algorithm
  - mind memory optimization

# Notes
- Workload optimization can basically be ignored on startup