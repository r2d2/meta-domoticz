DESCRIPTION = "iptables custom rules & persistent"
LICENSE = "CLOSED"
PR="r0"

# No information for SRC_URI yet (only an external source tree was specified)
SRC_URI = " \
	file://rules.v4         \
	file://rules.v6         \
        file://iptables.service \
	"

RDEPENDS_${PN} = " iptables"

inherit systemd

SYSTEMD_SERVICE_${PN} = "iptables.service"

do_install() {
        install -d ${D}${systemd_unitdir}/system/

	install -m 0644 ${WORKDIR}/domoticz.service  ${D}${systemd_unitdir}/system
}


do_install() {
        install -d ${D}${sysconfdir}/iptables
	install -m 0644 ${WORKDIR}/rules.v4  ${D}${sysconfdir}/iptables/
	install -m 0644 ${WORKDIR}/rules.v6  ${D}${sysconfdir}/iptables/

	install -d ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/iptables.service  ${D}${systemd_unitdir}/system
}
