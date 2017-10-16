enum class Status {
        OPENED, FINISHED
    }

	enum class Winner {
        HOME, AWAY, DRAW
    }

    
    data class Match(
     var date          : String?,
     var status        : Status?,
     var homeTeamName  : String = "",
     var awayTeamName  : String = "",
     var goalsHomeTeam : Int    = 0,
     var goalsAwayTeam : Int    = 0) {
        var winner: Winner = Winner.AWAY
        	get() {
                if (this.goalsHomeTeam == this.goalsAwayTeam) {
                    return Winner.DRAW
                }
                return if (this.goalsHomeTeam > this.goalsAwayTeam) Winner.HOME else Winner.AWAY
            }
    }
