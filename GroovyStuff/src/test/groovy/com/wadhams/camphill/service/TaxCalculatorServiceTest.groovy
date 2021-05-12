package com.wadhams.camphill.service

import spock.lang.Specification

class TaxCalculatorServiceTest extends Specification {
	def "level 5 2019-20 without Medicare Levy"() {
        setup:
        def service = new TaxCalculatorService()

        when:
        def result = service.calculate(new BigDecimal('333000'), '2019-20')

        then:
        result == new BigDecimal('122947')
    }
	def "level 4 2019-20 without Medicare Levy"() {
        setup:
        def service = new TaxCalculatorService()

        when:
        def result = service.calculate(new BigDecimal('150000'), '2019-20')

        then:
        result == new BigDecimal('42997')
    }
	def "level 3 2019-20 without Medicare Levy"() {
        setup:
        def service = new TaxCalculatorService()

        when:
        def result = service.calculate(new BigDecimal('50000'), '2019-20')

        then:
        result == new BigDecimal('7797')
    }
	def "level 2 2019-20 without Medicare Levy"() {
        setup:
        def service = new TaxCalculatorService()

        when:
        def result = service.calculate(new BigDecimal('25000'), '2019-20')

        then:
        result == new BigDecimal('1292')
    }
	def "level 1 2019-20 without Medicare Levy"() {
        setup:
        def service = new TaxCalculatorService()

        when:
        def result = service.calculate(new BigDecimal('12000'), '2019-20')

        then:
        result == new BigDecimal('0')
    }
}
