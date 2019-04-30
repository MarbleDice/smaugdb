# Notes on Smaug behavior

## Cost and shops

Money cannot have value[0] < 1

Objects popped in room have cost set to 0

Sale prices modified by
* +15% for being rich
* -7% to +15% based on level (0% at 20)
* -1% per cha above 13
* -1% per level below 20 (applied after checking purchase margin)
* -9.09% to +11.11% based on race (applied after checking purchase margin)

Purchase prices modified by
* -15% for being rich
* +1% per cha above 13
* -% for staves and wands missing charges

## Mob progs

Interesting commands include:
* c[ast] <spell>
* mpdamage
* mpforce (remove, drop, sac)
* mpjunk
* mpoload / mpmload
* mppurge
* mprestore
* mptrans[fer]
