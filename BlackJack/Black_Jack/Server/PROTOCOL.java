package Server;
public interface PROTOCOL {
	// 메세지 분석
	static final String CHATTING = "100";
	//static final String GAME = "200";
	static final String WAIT = "300";
	static final String PROHIBIT = "400";
	static final String PLAYER = "500";
	
	static final String READY = "201";
	static final String BET = "202";
	static final String GETCARD = "203";
	static final String STAY = "204";
	static final String HIT = "205";
	static final String BURST = "206";
	static final String RESULT = "207";
	static final String RESTART = "208";
}
