package sua.autonomouscar.interfaces;

public enum EFaceStatus {

	LOOKING_FORWARD(0),
	DISTRACTED(1),
	SLEEPING(2),
	UNKNOWN(3);
	

	protected int code;
	
	private EFaceStatus(int code) {
		this.code = code;
	}
	
	
	protected int getCode() {
		return this.code;
	}
	
	public EFaceStatus getStatus(int code) {
		for(EFaceStatus s : EFaceStatus.values() ) {
			if ( s.getCode() == code )
				return s;
		}
		return EFaceStatus.UNKNOWN;
	}
}
