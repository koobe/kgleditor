package kgl

class User {

	transient springSecurityService

	String username
	String password

    String fullName
    String email

    String description

    //TODO
    // loginType

	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    static hasMany = [oAuthIDs: OAuthID]

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
        description nullable: true, maxSize: 1024 * 1024
		email nullable: true
	}

	static mapping = {
		table '`user`'
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
