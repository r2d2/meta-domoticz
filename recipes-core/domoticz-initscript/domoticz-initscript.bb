DESCRIPTION = "Domoticz init script"
LICENSE = "CLOSED"
PR="r0"

inherit systemd

SRC_URI = " \
	file://domoticz.service   \
	"

SYSTEMD_SERVICE_${PN} = "domoticz.service"

do_install() {
        install -d ${D}${systemd_unitdir}/system/

	install -m 0644 ${WORKDIR}/domoticz.service  ${D}${systemd_unitdir}/system

}
