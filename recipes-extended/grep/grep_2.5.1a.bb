SUMMARY = "Pattern matching utilities"
DESCRIPTION = "The GNU versions of commonly used grep utilities.  The grep command searches one or more input \
files for lines containing a match to a specified pattern."
SECTION = "console/utils"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r2"

SRC_URI = "${GNU_MIRROR}/grep/grep-${PV}.tar.bz2 \
           file://uclibc-fix.patch \
           file://grep_fix_for_automake-1.12.patch \
           file://gettext.patch \
           file://fix64-int-to-pointer.patch \
           file://Makevars \
           file://grep-CVE-2012-5667.patch \
           file://fix-for-texinfo-5.1.patch \
           file://grep-egrep-fgrep-Fix-LSB-NG-cases.patch \
           file://search-fix-compilation-error-with-security-flags-ena.patch \
           file://0001-Fix-builds-with-gettext-0.20.patch \
           file://autoconf270.patch \
           "

SRC_URI[md5sum] = "52202fe462770fa6be1bb667bd6cf30c"
SRC_URI[sha256sum] = "38c8a2bb9223d1fb1b10bdd607cf44830afc92fd451ac4cd07619bf92bdd3132"

inherit autotools gettext texinfo

EXTRA_OECONF_INCLUDED_REGEX = "--without-included-regex"
EXTRA_OECONF_INCLUDED_REGEX:libc-musl = "--with-included-regex"

EXTRA_OECONF = "--disable-perl-regexp \
                ${EXTRA_OECONF_INCLUDED_REGEX}"

CFLAGS += "-D PROTOTYPES"
do_configure:prepend () {
	rm -f ${S}/m4/init.m4
	cp -f ${WORKDIR}/Makevars ${S}/po/
}

do_install () {
	autotools_do_install
        if [ "${base_bindir}" != "${bindir}" ]; then
        	install -d ${D}${base_bindir}
	        mv ${D}${bindir}/grep ${D}${base_bindir}/grep
	        mv ${D}${bindir}/egrep ${D}${base_bindir}/egrep
	        mv ${D}${bindir}/fgrep ${D}${base_bindir}/fgrep
	        rmdir ${D}${bindir}/
        fi
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE:${PN} = "grep egrep fgrep"
ALTERNATIVE_LINK_NAME[grep] = "${base_bindir}/grep"
ALTERNATIVE_LINK_NAME[egrep] = "${base_bindir}/egrep"
ALTERNATIVE_LINK_NAME[fgrep] = "${base_bindir}/fgrep"

export CONFIG_SHELL = "/bin/sh"
