__author__ = 'Administrator'

import os
import settings


class DependencyCalculator:
    def __init__(self):
        print("Init Dependency Calculator...")

    def loadFile(self):
        pass

    # 根据是否在同一个测试用例中计算
    def calcByTestCase(self):
        pass

    # 根据是否操作同一资源计算
    def calcByResource(self):
        pass

    # 根据是否属于同一服务计算
    def calcByService(self):
        pass
